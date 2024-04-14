package study.stepup.productaccountingservice.models;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
@JsonRootName(value = "data")
public class ProductAccountingResponse
{
    private String accountId;
}
