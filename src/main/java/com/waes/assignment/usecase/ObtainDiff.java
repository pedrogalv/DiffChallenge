package com.waes.assignment.usecase;

import com.waes.assignment.domain.Diff;
import com.waes.assignment.domain.DiffResponse;
import com.waes.assignment.domain.exceptions.DiffException;
import com.waes.assignment.gateway.repository.DiffRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import static com.waes.assignment.domain.DiffSide.LEFT;
import static com.waes.assignment.domain.DiffSide.RIGHT;

/**
 * Created by Pedro on 26/05/2017.
 *
 * UseCase responsible for obtaining the diff between the two diffs already saved. In case of missing diffs, it throws a
 * DiffException
 */
@Slf4j
@Component
public class ObtainDiff {

    private static final String EQUAL = "Left and Right data are equal";
    private static final String NOT_EQUAL_SIZE = "Left and Right data do not have the same size";
    private static final String NOT_EQUAL = "Left and Right are not equal";

    private static final String ERROR_NOT_NULL = "Error: {0} diff was not provided";
    private static final String DIFF_INFO = "Offset: {0}, Length: {1}";

    private final DiffRepository diffRepository;

    @Autowired
    public ObtainDiff(DiffRepository diffRepository) {
        this.diffRepository = diffRepository;
    }

    public DiffResponse execute(Long id){
        log.info("Obtaining diff for ID {}", id);
        Diff leftDiff = diffRepository.getByIdAndAndDiffSide(id, LEFT);
        Diff rightDiff = diffRepository.getByIdAndAndDiffSide(id, RIGHT);
        return diff(leftDiff, rightDiff);
    }

    private DiffResponse diff(Diff leftDiff, Diff rightDiff){

        assertDataIsNotNull(leftDiff, rightDiff);

        DiffResponse response = new DiffResponse();
        byte[] leftBytes = Base64.getDecoder().decode(leftDiff.getData());
        byte[] rightBytes = Base64.getDecoder().decode(rightDiff.getData());

        List<String> diffList = new ArrayList<>();

        if(leftBytes.length == rightBytes.length){
            int offset = -1;
            int length = 0;
            for(int i = 0; i < leftBytes.length; i ++){
                if(offset != -1 && leftBytes[i] == rightBytes[i]){
                    diffList.add(MessageFormat.format(DIFF_INFO, offset, length));
                    offset = -1;
                    length = 0;
                }

                if(leftBytes[i] != rightBytes[i]){
                    offset = offset == -1 ? i : offset;
                    length ++;
                }
            }

            if(offset != -1){
                diffList.add(MessageFormat.format(DIFF_INFO, offset, length));
            }

            if(diffList.isEmpty()){
                response.setMessage(EQUAL);
            } else {
                response.setMessage(NOT_EQUAL);
                response.setDiffs(diffList);
            }

        } else {
            response.setMessage(NOT_EQUAL_SIZE);
        }

        return response;
    }

    private void assertDataIsNotNull(Diff leftDiff, Diff rightDiff){
        if(leftDiff == null){
            throw new DiffException(MessageFormat.format(ERROR_NOT_NULL,LEFT.name()));
        }

        if(rightDiff == null){
            throw new DiffException(MessageFormat.format(ERROR_NOT_NULL,RIGHT.name()));
        }
    }
}
