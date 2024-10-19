package org.zerock.apiserver.domain;


import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable //이거뭐냐
@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductImage {
    private String fileName;

    private int ord;//순번

    public void setOrd(int ord) {
        this.ord = ord;
    }
}
