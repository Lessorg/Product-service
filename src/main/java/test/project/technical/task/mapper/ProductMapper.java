package test.project.technical_task.mapper;

import org.mapstruct.*;
import test.project.technical_task.config.MapperConfig;
import test.project.technical_task.dto.ProductRequestDto;
import test.project.technical_task.model.Product;

import java.time.LocalDateTime;

@Mapper(config = MapperConfig.class)
public interface ProductMapper {
    Product toEntity(ProductRequestDto productRequestDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateProductFromDto(ProductRequestDto dto, @MappingTarget Product entity);

    @AfterMapping
    default void setTimestamps(@MappingTarget Product product) {
        product.setLastUpdatedDate(LocalDateTime.now());
        if (product.getCreatedDate() == null) {
            product.setCreatedDate(LocalDateTime.now());
        }
    }
}
