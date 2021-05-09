package wooteco.subway.line.web;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import wooteco.subway.AcceptanceTest;

@DisplayName("노선 관련 기능")
class LineApiControllerTest extends AcceptanceTest {

    @DisplayName("노선 생성 - 성공")
    @Test
    void createLine() {
        // given
        final String lineName = "신분당선";
        final String lineColor = "bg-red-600";
        final LineRequest lineRequest = LineRequest.create(lineName, lineColor);

        // when
        final ExtractableResponse<Response> result = 노선_생성(lineRequest);

        //then
        final LineResponse lineResponse = result.body().as(LineResponse.class);

        assertThat(result.header("Location")).isNotEmpty();
        assertThat(lineResponse.getName()).isEqualTo(lineName);
        assertThat(lineResponse.getColor()).isEqualTo(lineColor);
    }

    @DisplayName("노선 생성 - 실패(이름 중복)")
    @Test
    void createLine_duplicatedName() {
        // given
        final String lineName = "신분당선";
        final String color = "bg-red-600";
        노선_생성(LineRequest.create(lineName, color));

        // when
        final ExtractableResponse<Response> result =
            노선_생성(LineRequest.create(lineName, color));

        // then
        assertThat(result.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("노선 목록 조회 - 성공")
    @Test
    void getLines() {
        /// given
        final LineRequest lineRequest1 = LineRequest.create("신분당선", "bg-red-600");
        final LineRequest lineRequest2 = LineRequest.create("2호선", "bg-green-600");
        노선_생성(lineRequest1);
        노선_생성(lineRequest2);

        // when
        final ExtractableResponse<Response> result = 노선_조회();

        // then
        final List<LineResponse> response = Arrays.asList(result.body().as(LineResponse[].class));

        assertThat(result.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response).hasSize(2);
        assertThat(response).extracting(LineResponse::getName).containsExactlyInAnyOrder("신분당선","2호선");
        assertThat(response).extracting(LineResponse::getColor).containsExactlyInAnyOrder("bg-red-600","bg-green-600");
    }

    @DisplayName("한 노선 조회 - 성공")
    @Test
    void getLineById() {
        // given
        final LineRequest lineRequest = LineRequest.create("신분당선", "bg-red-600");
        final ExtractableResponse<Response> createResponse =
            노선_생성(lineRequest);
        int lineId = createResponse.body().path("id");

        // when
        ExtractableResponse<Response> response = 노선_조회((long) lineId);

        // then
        final LineResponse lineResponse = response.body().as(LineResponse.class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(lineResponse.getName()).isEqualTo("신분당선");
        assertThat(lineResponse.getColor()).isEqualTo("bg-red-600");
    }



    @DisplayName("노선 조회 - 실패(노선 정보 없음)")
    @Test
    void getStationById_notFound() {
        /// given
        ExtractableResponse<Response> response = 노선_조회(Long.MAX_VALUE);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @DisplayName("노선 수정 - 성공")
    @Test
    void updateLine() {
        /// given
        String lineName = "신분당선";
        String lineColor = "bg-red-600";
        final String uri = 노선_생성(LineRequest.create(lineName, lineColor)).header("Location");

        String newLineName = "1호선";
        final LineRequest lineRequest = LineRequest.create(newLineName, lineColor);

        // when
        final ExtractableResponse<Response> result = 노선_수정(uri, lineRequest);

        // then
        final LineResponse lineResponse = 노선_조회(uri).as(LineResponse.class);

        assertThat(result.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(lineResponse.getName()).isEqualTo(newLineName);
        assertThat(lineResponse.getName()).isNotEqualTo(lineName);
        assertThat(lineResponse.getColor()).isEqualTo(lineColor);
    }

    @DisplayName("노선 수정 - 실패(변경하려는 노선 이름 중복)")
    @Test
    void updateLine_duplicatedName() {
        /// given
        String originalName = "구분당선";
        String originalColor = "bg-blue-600";
        노선_생성(LineRequest.create(originalName, originalColor));

        String targetName = "신분당선";
        String targetColor = "bg-red-600";
        final String uri = 노선_생성(LineRequest.create(targetName, targetColor)).header("Location");

        // when
        final LineRequest lineRequest = LineRequest.create("1호선", originalColor);
        final ExtractableResponse<Response> result = 노선_수정(uri, lineRequest);

        // then
        assertThat(result.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(result.body().asString()).isEqualTo("이미 등록되어 있는 노선 정보입니다.");
    }

    @DisplayName("노선 수정 - 실패(존재 하지 않는 노선 수정)")
    @Test
    void updateLine_notFound() {
        /// given
        final LineRequest lineRequest = LineRequest.create("구분당선", "bg-blue-600");

        // when
        final ExtractableResponse<Response> result = 노선_수정("/lines/" + Long.MAX_VALUE, lineRequest);

        // then
        assertThat(result.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @DisplayName("노선 삭제 - 성공")
    @Test
    void removeLine() {
        /// given
        final LineRequest lineRequest = LineRequest.create("신분당선", "bg-red-600");
        final String uri = 노선_생성(lineRequest).header("Location");

        // when
        final ExtractableResponse<Response> result = 노선_삭제(uri);

        // then
        assertThat(result.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        assertThat(노선_조회(uri).statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    private ExtractableResponse<Response> 노선_생성(LineRequest lineRequest) {
        return RestAssured
            .given()
            .body(lineRequest)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/lines")
            .then().log().all()
            .extract();
    }

    private ExtractableResponse<Response> 노선_조회() {
        return RestAssured
            .given()
            .when()
            .get("/lines")
            .then()
            .extract();
    }

    private ExtractableResponse<Response> 노선_조회(Long lineId) {
        return RestAssured.given().log().all()
            .when()
            .get("/lines/"+lineId)
            .then().log().all()
            .extract();
    }

    private ExtractableResponse<Response> 노선_조회(String uri) {
        return RestAssured.given().log().all()
            .when()
            .get(uri)
            .then().log().all()
            .extract();
    }

    private ExtractableResponse<Response> 노선_수정(String uri, LineRequest lineRequest) {
        return RestAssured.given().log().all()
            .body(lineRequest)
            .contentType(ContentType.JSON)
            .when()
            .put(uri)
            .then().extract();
    }

    private ExtractableResponse<Response> 노선_삭제(String uri) {
        return RestAssured.given().log().all()
            .when()
            .delete(uri)
            .then()
            .extract();
    }
}