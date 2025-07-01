package com.careerflow.jobms.job;

import java.util.List;

import com.careerflow.jobms.job.dto.JobWithCompanyDTO;

public interface JobService {
    List<JobWithCompanyDTO> findAll();
    void createJob(Job job);
    JobWithCompanyDTO getJobById(Long id);
    boolean deleteJob(Long id);
    boolean updateJob(Long id, Job updatedJob);
} 