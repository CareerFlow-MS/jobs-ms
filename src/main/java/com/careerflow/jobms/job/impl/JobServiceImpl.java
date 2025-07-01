package com.careerflow.jobms.job.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.careerflow.jobms.job.Job;
import com.careerflow.jobms.job.JobRepository;
import com.careerflow.jobms.job.JobService;
import com.careerflow.jobms.job.dto.JobWithCompanyDTO;
import com.careerflow.jobms.job.external.Company;



@Service
public class JobServiceImpl implements JobService{

    JobRepository jobRepository;

    @Autowired
    RestTemplate restTemplate;


    public JobServiceImpl(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    

    @Override
    public List<JobWithCompanyDTO> findAll() {

        List<Job> jobs = jobRepository.findAll();
        List<JobWithCompanyDTO> jobWithCompanyDTOs = new ArrayList<>();

        return jobs.stream()
                .map(this::convertDto).collect(Collectors.toList());

    }

    private JobWithCompanyDTO convertDto(Job job){
            JobWithCompanyDTO jobWithCompanyDTO = new JobWithCompanyDTO();
            jobWithCompanyDTO.setJob(job);
            
            // Fetch company details from the external service
            //RestTemplate restTemplate = new RestTemplate();
            Company company = restTemplate.getForObject("http://COMPANYMS:8081/companies/" + job.getCompanyId(), Company.class);
            if (company != null) {
                jobWithCompanyDTO.setCompany(company);
            } else {
                System.out.println("Company not found for job id: " + job.getId());
            }
            return jobWithCompanyDTO;

    }

    @Override
    public void createJob(Job job) {
        jobRepository.save(job);
    }

    @Override
    public JobWithCompanyDTO getJobById(Long id) {
       Job job =  jobRepository.findById(id).orElse(null);
       return convertDto(job);

    }

    @Override
    public boolean deleteJob(Long id) {
        if (jobRepository.existsById(id)) {
            jobRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public boolean updateJob(Long id, Job updatedJob) {
        Job existingJob = jobRepository.findById(id).orElse(null);
        if (existingJob != null) {
            updatedJob.setId(id);
            jobRepository.save(updatedJob);
            return true;
        }
        return false;
    }

    
}
