package org.ai.roboadvisor.global.swagger_annotation;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.ai.roboadvisor.global.common.dto.ErrorApiResponse;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@ApiResponse(responseCode = "500",
        description = "서버 내부 오류",
        content = @Content(schema = @Schema(implementation = ErrorApiResponse.class),
                examples = @ExampleObject(name = "Internal Server Error",
                        value = """
                                    {
                                        "code" : 500,
                                        "message" : "서버 내부 오류",
                                        "data" : null
                                    }
                                """
                )))
public @interface ApiResponse_Internal_Server_Error {
}
