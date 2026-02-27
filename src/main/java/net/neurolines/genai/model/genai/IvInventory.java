package net.neurolines.genai.model.genai;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class IvInventory {
    private int iviRegno; // 고유번호
    private int gubun; // 품목구분(1:원료, 2:소모품, 3:시약, 4:제품, 5:반제품)
    private int type; // 1:제조/2:내수/3:수입
    private String productCode; // 자재코드
    private String productCodeName; // 자재코드명
    private String productId; // 자재 ID
    private String vendorCode; // 벤더 코드
    private String msdsNo; // msds 번호
    private String productName; // 제품명
    private String companyName; // 회사명
    private String tel; // 전화번호
    private String word; // 신호어
    private String pscApr; // 외관
    private String pscMatter; // 성상
    private String pscColor; // 색상
    private String pscOdour; // 냄새
    private String pscOdourThreshold; // 냄새역치
    private String pscPh; // ph
    private String pscMeltingPoint; // 녹는점/어느점
    private String pscBoilingPoint; // 끓는점/끓는점 범위
    private String pscFlashPoint; // 인화점
    private String pscEvaporationRate; // 증발속도
    private String pscFlammable; // 인화성(고체, 기체)
    private String pscExplosionMin; // 폭발범위 하한
    private String pscExplosionMax; // 폭발범위 상한
    private String pscAporPressure; // 증기압
    private String pscSolubility; // 용해도
    private String pscVapourDensity; // 증기밀도
    private String pscRelativeDensity; // 비중(상대밀도)
    private String pscPartitionCoefficient; // n-옥탄올/물분배계수
    private String pscAutoTemperature; // 자연발화온도
    private String pscDecompositionTemperature; // 분해온도
    private String pscViscosity; // 점도
    private String pscMolecularWeight; // 분자량
    private String version; // 버전
    private int firstWriteDate; // 최초작성일자
    private int revisionDate; // 개정일자
    private int regYmd; // MSDS 업로드 등록일
    private String errorMsg; // 에러메세지
    private int status; // 상태(0:등록 진행중, 1:등록완료, 99:등록불가)
    private int statusConfirm; // 검증여부(0:미진행, 1:검증완료)
    private String statusHistoryYn; // 히스토리 : Y, 최신msds : N
    private String revisionYn; // 개정 여부(신규 : N, 개정 : Y)
    private String registrationName; // 등록자
    private String activeYn; // 활성화 여부
}