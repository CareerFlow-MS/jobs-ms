package com.careerflow.jobms.job;

import java.util.List;

import com.careerflow.jobms.job.dto.JobDTO;

public interface JobService {
    List<JobDTO> findAll();
    void createJob(Job job);
    JobDTO getJobById(Long id);
    boolean deleteJob(Long id);
    boolean updateJob(Long id, Job updatedJob);
} 