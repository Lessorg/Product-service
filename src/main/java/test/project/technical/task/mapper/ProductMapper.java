package test.project.technical.task.mapper;

import java.time.LocalDateTime;
import org.mapstruct.AfterMapping;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import test.project.technical.task.config.MapperConfig;
import test.project.technical.task.dto.ProductRequestDto;
import test.project.technical.task.model.Product;

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
