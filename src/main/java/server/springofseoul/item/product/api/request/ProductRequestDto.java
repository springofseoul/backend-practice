package server.springofseoul.item.product.api.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor // JSON 역직렬화(Deserialization)를 위해 기본 생성자가 필요
public class ProductRequestDto {

    private String name;
    private int price;
    private int stockQuantity;
    private String imageUrl;
    private String description;
}