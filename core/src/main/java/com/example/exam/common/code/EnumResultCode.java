package com.example.exam.common.code;

public enum EnumResultCode {
	R00001 { // 성공
		@Override
		public String toString(){ return "R00001";}
		@Override
		public String getMsg(){ return "SUCCESS"; }
		@Override
		public boolean equalTo(String rtCd) { return this.toString().equals(rtCd); }
	},
	R99999 { // 시스템 오류
		@Override
		public String toString(){ return "R99999";}
		@Override
		public String getMsg(){ return "ERROR"; }
		@Override
		public boolean equalTo(String rtCd) { return this.toString().equals(rtCd); }
	},
	R11112 { // 데이터 없음
		@Override
		public String toString(){ return "R11112";}
		@Override
		public String getMsg(){ return "No Data"; }
		@Override
		public boolean equalTo(String rtCd) { return this.toString().equals(rtCd); }
	},
	R11113 { // 고객 검증 불일치
		@Override
		public String toString(){ return "R11113";}
		@Override
		public String getMsg(){ return "No Customer"; }
		@Override
		public boolean equalTo(String rtCd) { return this.toString().equals(rtCd); }
	},
	R11114 { // 필수값 누락
		@Override
		public String toString(){ return "R11114";}
		@Override
		public String getMsg(){ return "Check Required Parameters"; }
		@Override
		public boolean equalTo(String rtCd) { return this.toString().equals(rtCd); }
	};

	// 에러메시지
	public abstract String getMsg();
	// 결과코드 동일 여부
	public abstract boolean equalTo(String rtCd);
	// 원소 조회
	public static EnumResultCode valueOf(int n){
		return EnumResultCode.values()[n];
	}
}