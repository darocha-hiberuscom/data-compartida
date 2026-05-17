# tnd-msa-sp-orqclientes0035

Proyecto generado automáticamente con MCP WSDL Generator v2.3

## Estructura del Proyecto

```
tnd-msa-sp-orqclientes0035/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/pichincha/
│   │   │       ├── domain/
│   │   │       ├── application/
│   │   │       └── infrastructure/
│   │   └── resources/
│   │       └── legacy/          # Archivos WSDL y bindings aquí
│   └── test/
├── gradle/
│   ├── wrapper/
│   │   └── jaxb-bindings.xml   # Bindings JAXB globales
│   └── postProcessWsdl.groovy  # Script de post-procesamiento
├── build.gradle
└── README.md
```

## Comandos Útiles

### Generar clases desde WSDL
```bash
./gradlew generateFromWsdl
```

### Compilar el proyecto
```bash
./gradlew build
```

### Ejecutar tests
```bash
./gradlew test
```

### Ver reporte de cobertura
```bash
./gradlew jacocoTestReport
open build/reports/jacoco/test/html/index.html
```

## Características

- ✅ Generación automática de clases desde WSDL
- ✅ **Soporte para bindings JAXB específicos por WSDL**
- ✅ Renombrado automático de BodyIn/BodyOut a nombres específicos
- ✅ Manejo de clases compartidas por múltiples métodos
- ✅ Post-procesamiento externo para mejor mantenibilidad
- ✅ **Gradle wrapper incluido** - No requiere instalación local de Gradle
- ✅ Cobertura de código con Jacoco (100% requerido)
- ✅ Spring Boot 3.5.9
- ✅ Java 21

## Personalizar Nombres de Clases Generadas

### Problema Común

Por defecto, CXF genera clases basándose en los **tipos XSD** (ej: `entrada`, `salida`), pero a veces necesitas nombres más descriptivos basados en los **elementos** (ej: `ConsultarDescripcionError01Request`).

### Solución: Bindings JAXB Específicos

1. **Crear archivo de bindings** para tu WSDL:
   - Ubicación: `src/main/resources/legacy/{NombreWSDL}-bindings.xml`
   - Ejemplo: `src/main/resources/legacy/MiServicio-bindings.xml`

2. **Contenido del archivo de bindings**:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<jaxb:bindings xmlns:jaxb="https://jakarta.ee/xml/ns/jaxb"
               xmlns:xs="http://www.w3.org/2001/XMLSchema"
               version="3.0">
    
    <jaxb:bindings schemaLocation="TuEsquema.xsd" node="/xs:schema">
        <!-- Renombrar tipo 'entrada' a 'MiMetodoRequest' -->
        <jaxb:bindings node="//xs:complexType[@name='entrada']">
            <jaxb:class name="MiMetodoRequest"/>
        </jaxb:bindings>
        
        <!-- Renombrar tipo 'salida' a 'MiMetodoResponse' -->
        <jaxb:bindings node="//xs:complexType[@name='salida']">
            <jaxb:class name="MiMetodoResponse"/>
        </jaxb:bindings>
    </jaxb:bindings>
    
</jaxb:bindings>
```

3. **Generar clases**:

```bash
./gradlew generateFromWsdl
```

Las clases generadas tendrán los nombres personalizados.

### Ejemplo Completo

Para casos complejos con múltiples tipos a renombrar, puedes crear bindings más elaborados que mapeen cada tipo XSD a un nombre de clase específico. El archivo de bindings se detecta automáticamente si sigue el patrón: `{NombreWSDL}-bindings.xml`

## Notas

- Los archivos WSDL deben colocarse en `src/main/resources/legacy/`
- Los bindings específicos son opcionales (si no existen, se usan nombres por defecto)
- El binding global (`gradle/wrapper/jaxb-bindings.xml`) se aplica siempre
- El post-procesamiento se ejecuta automáticamente después de generar las clases
- Las clases generadas se encuentran en `build/generated/sources/wsdl/`
- **No necesitas instalar Gradle**: El proyecto incluye todos los archivos necesarios del wrapper

## Troubleshooting

### Las clases tienen nombres genéricos (Entrada, Salida)

**Solución**: Crear un archivo de bindings específico para tu WSDL (ver sección "Personalizar Nombres de Clases Generadas")

### Error: "Cannot find the declaration of element"

**Solución**: Verificar que el `schemaLocation` en el binding coincida con el nombre del archivo XSD

### Ver documentación completa

Consultar: `gradle/wrapper/SOLUCION-NOMBRES-CLASES.md`
