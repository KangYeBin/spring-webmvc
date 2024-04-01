package com.spring.mvc.chap05.common;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @ToString @EqualsAndHashCode
public class PageMaker {

	// 페이지 시작번호와 끝번호 (1~10, 11~20)
	private int begin, end, finalPage; // 보정된 끝 페이지 번호

	// 이전, 다음 버튼 활성화 여부
	private boolean prev, next;

	// 현재 사용자가 요청한 페이지 정보.
	private Page page;

	// 총 게시물 수
	private int totalCount;

	public PageMaker(Page page, int totalCount) {
		this.page = page;
		this.totalCount = totalCount;

		makePageInfo();
	}

	// 한 화면에 보여질 페이지 버튼 개수
	private static final int PAGE_COUNT = 10;

	private void makePageInfo() {

		// 끝 페이지 번호 (end) 계산
		this.end = (int) (Math.ceil((double)page.getPageNo() / PAGE_COUNT) * PAGE_COUNT);

		// 시작 페이지 번호 (begin) 계산
		this.begin = end - PAGE_COUNT + 1;

		// 이전 버튼 활성화 여부 (prev)
		//        this.prev = begin == 1 ? false : true;
		this.prev = begin > 1;

		// 찐막 페이지를 먼저 구하기
		this.finalPage = (int) Math.ceil((double) totalCount / page.getAmount());

		// 마지막 페이지 구간에서 end값을 finalPage 값으로 변경
		if (this.finalPage < this.end) {
			this.end = this.finalPage;
		}

		// 다음 버튼 활성화 여부 (next)
		this.next = this.end < this.finalPage;


       /* 메모장에 작성된 공식 내용.

        // next 여부 구하기
        this.next = this.end * page.getAmount() >= totalCount ? false : true;

        if(!next) {
            this.end = (int) Math.ceil((double) totalCount / page.getAmount());
        }

        */

	}
}