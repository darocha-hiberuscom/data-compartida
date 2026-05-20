package com.pichincha.sp.application.service;
import com.pichincha.sp.application.port.input.CreateCustomerInput;
import com.pichincha.sp.application.port.output.ApplicationLogger;
import com.pichincha.sp.application.service.orchestrator.CreateCustomerOrchestrator;
import com.pichincha.sp.domain.constants.CustomerFlowConstants;
import com.pichincha.sp.domain.dto.CreateCustomerInputDto;
import com.pichincha.sp.domain.dto.CreateCustomerResultDto;
import com.pichincha.sp.domain.exception.BusinessException;
import com.pichincha.sp.infrastructure.logging.annotation.BpLogger;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
importpackage com.pichincha.sp.application.service;
import com.pichincha.sp.application.port.input.CreateCustomerInput;
import com.pichincha.sp.application.port.output.ApplicationLograimport com.pichincha.sp.application.port.inpo import com.pichincha.sp.application.port.output.ApplicationLogger;reimport com.pichincha.sp.application.service.orchestrator.CreateCutaimport com.pichincha.sp.domain.constants.CustomerFlowConstants;
import com.pichinchcaimport com.pichincha.sp.domain.dto.CreateCustomerInputDto;
impteimport com.pichincha.sp.domain.dto.CreateCustomerResultDtviimport com.pichincha.sp.domain.exception.BusinessExceptionteimport com.pichincha.sp.infrastructure.logging.annotation.atimport jakarta.validation.ConstraintViolation;
import jakarta.valir;import jakarta.validation.Valid;
import jakarCrimport jakarta.validation.Valid@Vimport lombok.RequiredArgsConstruct) import org.springframework.stereotypeatimport org.springframework.validation.annotatliimportpackage com.pichincha.sp.application.service;
import  import com.pichincha.sp.application.port.input.Creolimport com.pichincha.sp.application.port.output.ApplicationLograimogimport com.pichinchcaimport com.pichincha.sp.domain.dto.CreateCustomerInputDto;
impteimport com.pichincha.sp.domain.dto.CreateCustomerResultDtviimport com.pichincha.sp.domain.exception.BusinessExceptionteimport com.pichincha.sp.infrastructure.logging.annotation.atimport jakarta.validation.ConstraintViolatigeimpteimport com.pichincha.sp.domain.dto.CreateCustomerResultDtviimport com.picerimport jakarta.valir;import jakarta.validation.Valid;
import jakarCrimport jakarta.validation.Valid@Vimport lombok.RequiredArgsConstruct) import org.springframework.stereotypeatimport org.springframework.validation.annotatliimporusimport jakarCrimport jakarta.validation.Valid@Vimporaiimport  import com.pichincha.sp.application.port.input.Creolimport com.pichincha.sp.application.port.output.ApplicationLograimogimport com.pichinchcaimport com.pichincha.sp.domain.dto.CreateCustomerInputDto;
impteimport tMimpteimport com.pichincha.sp.domain.dto.CreateCustomerResultDtviimport com.pichincha.sp.domain.exception.BusinessExceptionteimport com.pichincha.sp.infrastructure.logging.annotation.atimport jakart           import jakarCrimport jakarta.validation.Valid@Vimport lombok.RequiredArgsConstruct) import org.springframework.stereorm /Users/darochahiberus.com/Documents/GitHub/data-compartida/tnd-msa-sp-wsclientes0108/src/main/java/com/pichincha/sp/infrastructure/input/adapter/rest/mapper/WsClientes0108SoapMapper.java
rm /Users/darochahiberus.com/Documents/GitHub/data-compartida/tnd-msa-sp-wsclientes0108/src/main/java/com/pichincha/sp/application/service/orchestrator/CreateCustomerOrchestrator.java
python3 - << 'PY'
import os
p='/Users/darochahiberus.com/Documents/GitHub/data-compartida/tnd-msa-sp-wsclientes0108/src/main/java/com/pichincha/sp/application/service/orchestrator/CreateCustomerOrchestrator.java'
if os.path.exists(p):
    os.remove(p)
print('exists_after=', os.path.exists(p))
PY
mkdir -p /Users/darochahiberus.com/Documents/GitHub/data-compartida/tnd-msa-sp-wsclientes0108/src/main/java/com/pichincha/sp/application/mappers
mv /Users/darochahiberus.com/Documents/GitHub/data-compartida/tnd-msa-sp-wsclientes0108/src/main/java/com/pichincha/sp/application/service/builder/CreateCustomerRequestBuilder.java /Users/darochahiberus.com/Documents/GitHub/data-compartida/tnd-msa-sp-wsclientes0108/src/main/java/com/pichincha/sp/application/mappers/CreateCustomerRequestBuilder.java
mv '/Users/darochahiberus.com/Documents/GitHub/data-compartida/tnd-msa-sp-wsclientes0108/src/main/java/com/pichincha/sp/application/service/builder/CreateCustomerRequestBuilder.java' '/Users/darochahiberus.com/Documents/GitHub/data-compartida/tnd-msa-sp-wsclientes0108/src/main/java/com/pichincha/sp/application/mappers/CreateCustomerRequestBuilder.java' && ls -la '/Users/darochahiberus.com/Documents/GitHub/data-compartida/tnd-msa-sp-wsclientes0108/src/main/java/com/pichincha/sp/application/mappers/'
rm -f /Users/darochahiberus.com/Documents/GitHub/data-compartida/tnd-msa-sp-wsclientes0108/src/main/java/com/pichincha/sp/application/service/builder/CreateCustomerRequestBuilder.java && rmdir /Users/darochahiberus.com/Documents/GitHub/data-compartida/tnd-msa-sp-wsclientes0108/src/main/java/com/pichincha/sp/application/service/builder 2>/dev/null || true
ls -la /Users/darochahiberus.com/Documents/GitHub/data-compartida/tnd-msa-sp-wsclientes0108/src/main/java/com/pichincha/sp/application/service/builder/ 2>/dev/null || echo "builder_not_exists"
python3 - <<'PY'
import os
p='/Users/darochahiberus.com/Documents/GitHub/data-compartida/tnd-msa-sp-wsclientes0108/src/main/java/com/pichincha/sp/application/service/builder/CreateCustomerRequestBuilder.java'
print('exists_before', os.path.exists(p))
if os.path.exists(p):
    os.remove(p)
print('exists_after', os.path.exists(p))
PY
find /Users/darochahiberus.com/Documents/GitHub/data-compartida/tnd-msa-sp-wsclientes0108/src/main/java/com/pichincha/sp/application -name "CreateCustomerRequestBuilder.java" -delete && echo "deleted" || echo "not_found"
find /Users/darochahiberus.com/Documents/GitHub/data-compartida/tnd-msa-sp-wsclientes0108/src/main/java/com/pichincha/sp/application -name "CreateCustomerRequestBuilder.java" | xargs cat 2>/dev/null
find /Users/darochahiberus.com/Documents/GitHub/data-compartida/tnd-msa-sp-wsclientes0108/src/main/java/com/pichincha/sp/application -name "CreateCustomerRequestBuilder.java" -print
find /Users/darochahiberus.com/Documents/GitHub/data-compartida/tnd-msa-sp-wsclientes0108/src/main/java/com/pichincha/sp/application -name "*.java" | sort
ls -laR /Users/darochahiberus.com/Documents/GitHub/data-compartida/tnd-msa-sp-wsclientes0108/src/main/java/com/pichincha/sp/application/
cat /Users/darochahiberus.com/Documents/GitHub/data-compartida/tnd-msa-sp-wsclientes0108/src/main/java/com/pichincha/sp/application/mappers/CreateCustomerRequestBuilder.java 2>/dev/null | head -5
ls -la /Users/darochahiberus.com/Documents/GitHub/data-compartida/tnd-msa-sp-wsclientes0108/src/main/java/com/pichincha/sp/application/mappers/ && echo "---" && ls -la /Users/darochahiberus.com/Documents/GitHub/data-compartida/tnd-msa-sp-wsclientes0108/src/main/java/com/pichincha/sp/application/service/
python3 -c "
import os, glob
base='/Users/darochahiberus.com/Documents/GitHub/data-compartida/tnd-msa-sp-wsclientes0108/src/main/java/com/pichincha/sp/application'
for root, dirs, files in os.walk(base):
    for f in files:
        print(os.path.join(root, f))
"
python3 << 'EOF'
import os
base = '/Users/darochahiberus.com/Documents/GitHub/data-compartida/tnd-msa-sp-wsclientes0108/src/main/java/com/pichincha/sp/application'
for root, dirs, files in os.walk(base):
    for f in files:
        full = os.path.join(root, f)
        print(full)
