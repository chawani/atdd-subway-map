package wooteco.subway.exception;

public class NotExistSectionException extends NotExistItemException {

    private static final String MESSAGE = "구간이 존재하지 않습니다.";

    public NotExistSectionException() {
        super(MESSAGE);
    }

}