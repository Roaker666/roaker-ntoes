package com.roaker.notes.uc.api.seq;

import com.roaker.notes.uc.controller.seq.LeafController;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author lei.rao
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
public class SeqApiImpl implements SeqApi{
    private final LeafController leafController;
    @Override
    public String getSegmentId(String key) {
        return leafController.getSegmentId(key);
    }
}
