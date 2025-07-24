package com.example.jobx_batch.utils;

import com.example.jobx_batch.dto.JobDto;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class XmlParserUtils {

    public List<String> extractJobCds(String responseXml) throws Exception {
        List<String> jobCdList = new ArrayList<>();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        InputStream inputStream = new ByteArrayInputStream(responseXml.getBytes("UTF-8"));
        Document document = builder.parse(inputStream);
        document.getDocumentElement().normalize();

        NodeList jobLists = document.getElementsByTagName("jobList");

        for (int i = 0; i < jobLists.getLength(); i++) {
            Element jobElement = (Element) jobLists.item(i);
            String jobCd = jobElement.getElementsByTagName("jobCd").item(0).getTextContent();
            jobCdList.add(jobCd);
        }

        return jobCdList;
    }


    public JobDto parse(String xml) throws Exception {
        JAXBContext context = JAXBContext.newInstance(JobDto.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        return (JobDto) unmarshaller.unmarshal(new StringReader(xml));
    }


}
