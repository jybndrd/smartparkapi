package com.demo.smartpark.mapper;

import org.mapstruct.Builder;
import org.mapstruct.MapperConfig;
import org.mapstruct.ReportingPolicy;

/**
 * Global configuration for Mapstruct mappers. Using this configuration on a mapper will
 * ignore reports for unmapped properties during the generation of its implementation.
 *
 * @author jandrada
 */
@MapperConfig(unmappedTargetPolicy = ReportingPolicy.IGNORE, builder = @Builder(disableBuilder = true))
public interface GlobalMapperConfig {

}
