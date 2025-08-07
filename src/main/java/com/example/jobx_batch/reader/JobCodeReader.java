package com.example.jobx_batch.reader;
import com.example.jobx_batch.client.EmploymentApiClient;
import com.example.jobx_batch.utils.XmlParserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemReader;

import java.util.Iterator;
import java.util.List;

@RequiredArgsConstructor
public class JobCodeReader implements ItemReader<String> {

    private final EmploymentApiClient employmentApiClient;
    private final String authKey;

    private Iterator<String> jobCodeIterator;

    @Override
    public String read() throws Exception {
        if (jobCodeIterator == null) {
            String target = "JOBCD";
            String apiUrl = String.format(
                    "https://www.work24.go.kr/cm/openApi/call/wk/callOpenApiSvcInfo212L01.do?authKey=%s&returnType=XML&target=%s",
                    authKey,
                    target
            );
            String responseXml = employmentApiClient.callEmploymentInfo(apiUrl);

            if (responseXml != null) {
                XmlParserUtils xmlParserUtils = new XmlParserUtils();
                List<String> jobCdList = xmlParserUtils.extractJobCds(responseXml);
                jobCodeIterator = jobCdList.iterator();
            } else {
                return null;
            }
        }

        if (jobCodeIterator.hasNext()) {
            return jobCodeIterator.next();
        } else {
            jobCodeIterator = null;
            return null;
        }
    }
}
