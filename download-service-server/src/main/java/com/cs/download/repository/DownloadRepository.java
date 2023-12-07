package com.cs.download.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cs.download.entity.DownloadEntity;

public interface DownloadRepository extends JpaRepository<DownloadEntity, Long> {
  List<DownloadEntity> findByUrl(String url);
}