#!/usr/bin/env groovy

/**
 * Script de post-procesamiento para clases generadas desde WSDL (REST)
 * 
 * Operaciones:
 * 1. Elimina clases JAX-WS innecesarias (interfaces, servicios, clientes)
 * 2. Normaliza nombres de clases siguiendo convenciones Java
 * 3. Detecta y procesa clases BodyIn/BodyOut (renombrado específico por método)
 * 4. Actualiza referencias entre clases
 * 5. Renombra archivos físicos
 * 
 * Uso: groovy postProcessWsdl.groovy <directorio-generado>
 */

class WsdlPostProcessor {

    static final String JAVA_EXT = '.java'

    def generatedDir
    def classNameMappings = [:]
    def bodyMappings = [:]
    def requestResponseMap = [:]
    
    WsdlPostProcessor(String dirPath) {
        this.generatedDir = new File(dirPath)
        if (!generatedDir.exists()) {
            throw new IllegalArgumentException("Directorio no existe: ${dirPath}")
        }
    }
    
    def process() {
        // Paso 1: Eliminar clases JAX-WS innecesarias
        removeUnwantedClasses()

        // Paso 2: Detectar clases que necesitan normalización
        detectClassesToNormalize()
        
        // Paso 3: Detectar y procesar BodyIn/BodyOut
        detectBodyClasses()
        processBodyClasses()
        
        // Paso 4: Actualizar referencias
        updateReferences()
        
        // Paso 5: Renombrar archivos
        renameFiles()
    }
    
    def removeUnwantedClasses() {
        // Eliminar clases JAX-WS innecesarias (cliente, interfaz, service)
        // Patrón genérico: buscar archivos que terminen en Request.java, _Service.java
        // o que sean interfaces JAX-WS generadas
        def deletedCount = 0
        
        generatedDir.listFiles().each { file ->
            if (file.name.endsWith(JAVA_EXT)) {
                def content = file.text
                
                // Detectar y eliminar clases de cliente JAX-WS (contienen @WebServiceClient)
                if (content.contains('@WebServiceClient') || 
                    content.contains('@WebService') && content.contains('interface')) {
                    file.delete()
                    deletedCount++
                    return
                }
                
                // Detectar y eliminar clases Service (contienen extends Service)
                if (file.name.endsWith('_Service.java') || 
                    (content.contains('extends Service') && content.contains('@WebServiceClient'))) {
                    file.delete()
                    deletedCount++
                    return
                }
            }
        }
    }

    def detectClassesToNormalize() {
        generatedDir.listFiles().each { file ->
            if (file.name.endsWith(JAVA_EXT)) {
                def fileName = file.name.replace(JAVA_EXT, '')
                def normalizedName = normalizeClassName(fileName)
                
                if (fileName != normalizedName) {
                    classNameMappings[fileName] = normalizedName
                }
            }
        }
    }
    
    def detectBodyClasses() {
        generatedDir.listFiles().each { file ->
            if (file.name.endsWith(JAVA_EXT)) {
                def fileName = file.name.replace(JAVA_EXT, '')
                def content = file.text
                
                // Buscar bodyIn y bodyOut
                def bodyInMatcher = content =~ /protected\s+([A-Z]\w*)\s+bodyIn;/
                def bodyOutMatcher = content =~ /protected\s+([A-Z]\w*)\s+bodyOut;/
                
                if (bodyInMatcher.find()) {
                    def bodyInClass = bodyInMatcher.group(1)
                    if (bodyInClass.toLowerCase().startsWith('bodyin')) {
                        def methodName = extractMethodName(fileName)
                        if (!requestResponseMap.containsKey(bodyInClass)) {
                            requestResponseMap[bodyInClass] = []
                        }
                        requestResponseMap[bodyInClass].add([
                            method: methodName, 
                            type: 'In', 
                            file: fileName
                        ])
                    }
                }
                
                if (bodyOutMatcher.find()) {
                    def bodyOutClass = bodyOutMatcher.group(1)
                    if (bodyOutClass.toLowerCase().startsWith('bodyout')) {
                        def methodName = extractMethodName(fileName)
                        if (!requestResponseMap.containsKey(bodyOutClass)) {
                            requestResponseMap[bodyOutClass] = []
                        }
                        requestResponseMap[bodyOutClass].add([
                            method: methodName, 
                            type: 'Out', 
                            file: fileName
                        ])
                    }
                }
            }
        }
    }
    
    def processBodyClasses() {
        requestResponseMap.each { oldClassName, usages ->
            if (usages.size() > 1) {
                // Clase compartida - duplicar
                usages.eachWithIndex { usage, index ->
                    def methodName = usage.method
                    def type = usage.type
                    def newClassName = "Body${type}${methodName}"
                    
                    if (index == 0) {
                        // Primera: solo marcar para renombrar
                        bodyMappings[oldClassName] = newClassName
                    } else {
                        // Resto: duplicar
                        duplicateClass(oldClassName, newClassName)
                    }
                    
                    // Actualizar referencia en el archivo específico
                    updateFileReference(usage.file, oldClassName, newClassName)
                }
            } else if (usages.size() == 1) {
                // Clase única - solo renombrar
                def usage = usages[0]
                def methodName = usage.method
                def type = usage.type
                def newClassName = "Body${type}${methodName}"
                
                if (oldClassName != newClassName) {
                    bodyMappings[oldClassName] = newClassName
                    updateFileReference(usage.file, oldClassName, newClassName)
                }
            }
        }
        
        // Combinar mapeos
        bodyMappings.each { oldName, newName ->
            classNameMappings[oldName] = newName
        }
    }
    
    def updateReferences() {
        def updated = 0
        generatedDir.listFiles().each { file ->
            if (file.name.endsWith(JAVA_EXT)) {
                def content = file.text
                def modified = false
                
                classNameMappings.each { oldName, newName ->
                    def pattern = "\\b${oldName}\\b"
                    if (content =~ pattern) {
                        content = content.replaceAll(pattern, newName)
                        modified = true
                    }
                }
                
                if (modified) {
                    file.text = content
                    updated++
                }
            }
        }
    }
    
    def renameFiles() {
        def renamed = 0
        classNameMappings.each { oldName, newName ->
            def oldFile = new File(generatedDir, "${oldName}.java")
            def newFile = new File(generatedDir, "${newName}.java")
            
            if (oldFile.exists() && !newFile.exists()) {
                oldFile.renameTo(newFile)
                renamed++
            }
        }
    }
    
    // Métodos auxiliares
    
    def normalizeClassName(String className) {
        // Normalizar nombres siguiendo convenciones Java
        if (className.contains('_')) {
            return className.split('_').collect { it.capitalize() }.join('')
        }
        
        // Capitalizar correctamente (ej: BodyIN -> BodyIn)
        if (className ==~ /Body(IN|OUT)\d*/) {
            return className.replaceAll(/(IN|OUT)/, { match ->
                match[0][0].toUpperCase() + match[0][1].toLowerCase()
            })
        }
        
        return className
    }
    
    def extractMethodName(String fileName) {
        // Quitar sufijos Request/Response
        return fileName.replaceAll('(Request|Response)$', '')
    }
    
    def duplicateClass(String oldClassName, String newClassName) {
        def originalFile = new File(generatedDir, "${oldClassName}.java")
        if (!originalFile.exists()) {
            return
        }
        
        def newFile = new File(generatedDir, "${newClassName}.java")
        def content = originalFile.text
        
        // Actualizar nombre de clase en el contenido
        content = content.replaceAll("\\bclass ${oldClassName}\\b", "class ${newClassName}")
        content = content.replaceAll("\\bpublic ${oldClassName}\\(", "public ${newClassName}(")
        
        newFile.text = content
    }
    
    def updateFileReference(String fileName, String oldClassName, String newClassName) {
        def file = new File(generatedDir, "${fileName}.java")
        if (!file.exists()) {
            return
        }
        
        def content = file.text
        def pattern = "\\b${oldClassName}\\b"
        
        if (content =~ pattern) {
            content = content.replaceAll(pattern, newClassName)
            file.text = content
        }
    }
}

// Punto de entrada
if (args.length == 0) {
    System.exit(1)
}

try {
    def processor = new WsdlPostProcessor(args[0])
    processor.process()
} catch (Exception e) {
    e.printStackTrace()
    System.exit(1)
}
