package com.careerflow.jobms.job.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.careerflow.jobms.job.Job;
import com.careerflow.jobms.job.JobRepository;
import com.careerflow.jobms.job.JobService;



@Service
public class JobServiceImpl implements JobService{

    JobRepository jobRepository;


    public JobServiceImpl(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    

    @Override
    public List<Job> findAll() {
        return jobRepository.findAll();
    }

    @Override
    public void createJob(Job job) {
        jobRepository.save(job);
    }

    @Override
    public Job getJobById(Long id) {
       return jobRepository.findById(id).orElse(null);

    }

    @Override
    public void deleteJob(Long id) {
        try{
            jobRepository.deleteById(id);
        } catch (Exception e) {
            System.out.println("Error deleting job with id: " + id);
        }
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
