package com.cs.download.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.download.entity.DownloadEntity;
import com.cs.download.repository.DownloadRepository;

@Service
public class DownloadEntityService {

  @Autowired
  private DownloadRepository DownloadRepository;

  public List<DownloadEntity> getAllEntities() {
    return DownloadRepository.findAll();
  }

  public DownloadEntity getEntityById(Long id) {
    return DownloadRepository.findById(id).orElse(null);
  }

  public Long saveEntity(DownloadEntity entity) {
    return DownloadRepository.save(entity).getId();
  }

  public void deleteEntity(Long id) {
    DownloadRepository.deleteById(id);
  }

  public List<DownloadEntity> findByUrl(String url) {
    return DownloadRepository.findByUrl(url);
  }
}
