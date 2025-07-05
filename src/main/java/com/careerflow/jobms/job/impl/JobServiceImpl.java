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
import com.careerflow.jobms.job.clients.CompanyClient;
import com.careerflow.jobms.job.clients.ReviewClient;
import com.careerflow.jobms.job.dto.JobDTO;
import com.careerflow.jobms.job.external.Company;
import com.careerflow.jobms.job.external.Review;
import com.careerflow.jobms.job.mapper.JobMapper;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;

import org.springframework.http.HttpMethod;



@Service
public class JobServiceImpl implements JobService{

    JobRepository jobRepository;

    @Autowired
    RestTemplate restTemplate;

    int attempt=0;

    private CompanyClient companyClient;
    private ReviewClient reviewClient;


    public JobServiceImpl(JobRepository jobRepository, CompanyClient companyClient, ReviewClient reviewClient) {
        this.jobRepository = jobRepository;
        this.companyClient = companyClient;
        this.reviewClient = reviewClient;
    }

    

    @Override
    //@CircuitBreaker(name = "companyBreaker", fallbackMethod = "companyBreakerFallback")
    //@Retry(name = "companyBreaker", fallbackMethod = "companyBreakerFallback")
    @RateLimiter(name = "companyBreaker", fallbackMethod = "companyBreakerFallback")
    public List<JobDTO> findAll() {

        System.out.println("Attempt : " +  ++attempt);
        List<Job> jobs = jobRepository.findAll();
        List<JobDTO> jobWithCompanyDTOs = new ArrayList<>();

        return jobs.stream()
                .map(this::convertDto).collect(Collectors.toList());

    }

    //Fallback method for the circuit breaker
    private List<String> companyBreakerFallback(Exception e) {
        List<String> list = new ArrayList<>();
        list.add("dummy");
        return list;
    }

    private JobDTO convertDto(Job job){
            // Fetch company details from the external service
            Company company = companyClient.getCompanyById(job.getCompanyId());
            List<Review> reviews = reviewClient.getReviews(job.getCompanyId());

            JobDTO jobDTO = JobMapper.mapToJobWithCompanyDTO(job, company, reviews);
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
