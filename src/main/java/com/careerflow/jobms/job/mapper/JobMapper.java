package com.careerflow.jobms.job.mapper;

import java.util.List;

import com.careerflow.jobms.job.Job;
import com.careerflow.jobms.job.dto.JobDTO;
import com.careerflow.jobms.job.external.Company;
import com.careerflow.jobms.job.external.Review;

public class JobMapper {
    public static JobDTO mapToJobWithCompanyDTO(Job job, Company company, List<Review> reviews) {
        JobDTO jobDTO = new JobDTO();
        jobDTO.setId(job.getId());
        jobDTO.setTitle(job.getTitle());
        jobDTO.setDescription(job.getDescription());
        jobDTO.setMinSalary(job.getMinSalary());
        jobDTO.setMaxSalary(job.getMaxSalary());
        jobDTO.setLocation(job.getLocation());
        jobDTO.setCompany(company);
        jobDTO.setReviews(reviews);
        return jobDTO;
    };
}
