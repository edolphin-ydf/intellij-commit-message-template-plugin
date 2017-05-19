package commitmessagetemplate.network;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by edolphin on 17-3-1.
 */
@Getter
@Setter
public class BaseResponse {

    String errorCode;

    String errorMessage;

    String status;

}
