package com.hzk.scan.a;

import com.hzk.scan.service.KService;
import com.hzk.scan.service.KServiceMethod;

@KService(group = "cloud.bos", name = "annotationDemo", appIds = {"*"}, transprotocalType = {"rpc",
        "http"}, dataCodec = {"json"}, description = "this is a annotation demo service")
public class AService {

    @KServiceMethod(name = "sayAnnotationOne", description = "this is a sayAnnotationOne")
    public String methodA(){
        return "A";
    }

    @KService(group = "cloud.bos", name = "annotationDemo", appIds = {"*"}, transprotocalType = {"rpc",
            "http"}, dataCodec = {"json"}, description = "this is a annotation demo service")
    public class InnerAService {}

}
