package com.careerflow.jobms.job.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.careerflow.jobms.job.external.Company;

@FeignClient(name = "companyms")
public interface CompanyClient {

    @GetMapping("/companies/{companyId}")
    Company getCompanyById(@PathVariable("companyId") Long id);
}
