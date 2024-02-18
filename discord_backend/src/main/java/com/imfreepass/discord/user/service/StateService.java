package com.imfreepass.discord.user.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.imfreepass.discord.user.api.request.AddState;
import com.imfreepass.discord.user.entity.State;
import com.imfreepass.discord.user.repository.StateRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StateService {
	
	private final StateRepository stateRepository;
	
	/**
	 * 상태 상세 조회 
	 * @param stateId
	 * @return
	 */
	public Optional<State> selectStateId(Long stateId) {
		return stateRepository.findById(stateId);
	}
	/**
	 * 상태 등록 
	 * @param req
	 * @return
	 */
	public State insert(AddState req) {
		State state = State.builder()
				.stateName(req.getStateName())
				.stateText(req.getStateText())
				.build();
	stateRepository.save(state);
	return null;
	}
	
	/* 메모 
	 * 
	 * 
	 * {
    "stateName" : "온라인"
		}
	{
    "stateName" : "자리 비움"
		}
	{
    "stateName" : "방해금지",
    "stateText" : "모든 알림을 받지 않아요"
	}
	{
    "stateName" : "오프라인",
    "stateText" : "온라인으로 표시되지는 않지만, Discord의 모든 기능을 이용할 수 있어요"
}
	 */
}
