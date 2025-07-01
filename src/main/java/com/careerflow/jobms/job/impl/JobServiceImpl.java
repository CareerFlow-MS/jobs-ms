package com.careerflow.jobms.job.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.careerflow.jobms.job.Job;
import com.careerflow.jobms.job.JobRepository;
import com.careerflow.jobms.job.JobService;
import com.careerflow.jobms.job.dto.JobDTO;
import com.careerflow.jobms.job.external.Company;
import com.careerflow.jobms.job.external.Review;
import com.careerflow.jobms.job.mapper.JobMapper;

import org.springframework.http.HttpMethod;



@Service
public class JobServiceImpl implements JobService{

    JobRepository jobRepository;

    @Autowired
    RestTemplate restTemplate;


    public JobServiceImpl(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    

    @Override
    public List<JobDTO> findAll() {

        List<Job> jobs = jobRepository.findAll();
        List<JobDTO> jobWithCompanyDTOs = new ArrayList<>();

        return jobs.stream()
                .map(this::convertDto).collect(Collectors.toList());

    }

    private JobDTO convertDto(Job job){
            // Fetch company details from the external service
            Company company = restTemplate.getForObject("http://COMPANYMS:8081/companies/" + job.getCompanyId(), Company.class);

            ResponseEntity<List<Review>> reviewResponse = restTemplate.exchange(
                "http://REVIEWMS:8083/reviews?companyId=" + job.getCompanyId(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Review>>() {}
            );

            List<Review> reviews = reviewResponse.getBody();



            JobDTO jobDTO = JobMapper.mapToJobWithCompanyDTO(job, company, reviews);
            // if (company != null) {
            //     jobDTO.setCompany(company);
            // } else {
            //     System.out.println("Company not found for job id: " + job.getId());
            // }
            return jobDTO;

    }

    @Override
    public void createJob(Job job) {
        jobRepository.save(job);
    }

    @Override
    public JobDTO getJobById(Long id) {
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
