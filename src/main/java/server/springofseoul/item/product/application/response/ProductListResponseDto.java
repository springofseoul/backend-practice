package server.springofseoul.item.product.application.response;

import server.springofseoul.item.product.domain.entity.Product;

public record ProductListResponseDto(
        long id,
        String name,
        int price,
        int stockQuantity,
        String imageUrl
) {
    public static ProductListResponseDto of(Product product) {
        return new ProductListResponseDto(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getStockQuantity(),
                product.getImageUrl()
        );
    }
}
