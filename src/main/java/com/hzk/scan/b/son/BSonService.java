package com.hzk.scan.b.son;

import com.hzk.scan.service.KService;

@KService(group = "cloud.bos", name = "annotationDemo", appIds = {"*"}, transprotocalType = {"rpc",
        "http"}, dataCodec = {"json"}, description = "this is a annotation demo service")
public class BSonService {
}
