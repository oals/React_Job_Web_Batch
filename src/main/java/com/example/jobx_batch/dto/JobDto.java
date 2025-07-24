package com.example.jobx_batch.dto;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "jobSum")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class JobDto {

    @XmlElement
    private String jobCd;

    @XmlElement
    private String jobLrclNm;

    @XmlElement
    private String jobMdclNm;

    @XmlElement
    private String jobSmclNm;

    @XmlElement
    private String jobSum;

    @XmlElement
    private String way;

    @XmlElement(name = "relMajorList")
    private List<RelMajor> relMajorList;

    @XmlElement(name = "relCertList")
    private List<RelCert> relCertList;

    @XmlElement
    private String sal;

    @XmlElement
    private String jobSatis;

    @XmlElement
    private String jobProspect;

    @XmlElement
    private String jobStatus;

    @XmlElement
    private String jobAbil;

    @XmlElement
    private String knowldg;

    @XmlElement
    private String jobEnv;

    @XmlElement
    private String jobChr;

    @XmlElement
    private String jobIntrst;

    @XmlElement
    private String jobVals;

    @XmlElement
    private String jobActvImprtncs;

    @XmlElement
    private String jobActvLvls;

    @XmlElement(name = "relJobList")
    private List<RelJob> relJobList;

    @XmlAccessorType(XmlAccessType.FIELD)
    @Data
    public static class RelMajor {
        @XmlElement
        private String majorCd;

        @XmlElement
        private String majorNm;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @Data
    public static class RelCert {
        @XmlElement
        private String certNm;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @Data
    public static class RelJob {
        @XmlElement
        private String jobCd;

        @XmlElement
        private String jobNm;
    }
}
