package com.careerflow.jobms.job;

import java.util.List;

import com.careerflow.jobms.job.dto.JobWithCompanyDTO;

public interface JobService {
    List<JobWithCompanyDTO> findAll();
    void createJob(Job job);
    Job getJobById(Long id);
    void deleteJob(Long id);
    boolean updateJob(Long id, Job updatedJob);
} 