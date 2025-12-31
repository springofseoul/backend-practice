package server.springofseoul.item.product.application.response;

import server.springofseoul.item.product.domain.entity.Product;

public record ProductResponseDto(
        long id,
        String name,
        int price,
        int stockQuantity,
        String imageUrl,
        String description
) {
    public static ProductResponseDto of(Product product) {
        return new ProductResponseDto(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getStockQuantity(),
                product.getImageUrl(),
                product.getDescription()
        );
    }
}
