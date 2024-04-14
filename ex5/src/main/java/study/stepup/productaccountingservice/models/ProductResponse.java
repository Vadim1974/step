package study.stepup.productaccountingservice.models;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonRootName(value = "data")
public class ProductResponse
{
    private String instanceId;
    private List<String> registerId;
    private List<String> supplementaryAgreementId;

}

