package com.poseidon.dolphin.config;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.poseidon.dolphin.member.Member;
import com.poseidon.dolphin.member.SocialType;
import com.poseidon.dolphin.member.service.MemberService;

public class SocialAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
	protected final Log logger = LogFactory.getLog(this.getClass());

	private RequestCache requestCache = new HttpSessionRequestCache();
	
	private final MemberService memberService;
	
	public SocialAuthenticationSuccessHandler(MemberService memberService) {
		this.memberService = memberService;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request,
			HttpServletResponse response, Authentication authentication)
			throws ServletException, IOException {
		
		OAuth2Authentication oauth2Authentication = (OAuth2Authentication) authentication;
		Map<String, Object> details = (Map<String, Object>) oauth2Authentication.getUserAuthentication().getDetails();
		String resultCode = (String)details.get("resultcode");
		if(resultCode.equals("00")) {
			Map<String, Object> naverResponse = (Map<String, Object>)details.get("response");
			String id = (String)naverResponse.get("id");
			Assert.notNull(id, "id must not be null");
			
			Member member = new Member();
			member.setUsername(id);
			member.setSocialType(SocialType.NAVER);
			memberService.saveChanges(member);
		}
		
		SavedRequest savedRequest = requestCache.getRequest(request, response);

		if (savedRequest == null) {
			super.onAuthenticationSuccess(request, response, authentication);

			return;
		}
		String targetUrlParameter = getTargetUrlParameter();
		if (isAlwaysUseDefaultTargetUrl()
				|| (targetUrlParameter != null && StringUtils.hasText(request
						.getParameter(targetUrlParameter)))) {
			requestCache.removeRequest(request, response);
			super.onAuthenticationSuccess(request, response, authentication);

			return;
		}

		clearAuthenticationAttributes(request);

		// Use the DefaultSavedRequest URL
		String targetUrl = savedRequest.getRedirectUrl();
		logger.debug("Redirecting to DefaultSavedRequest Url: " + targetUrl);
		getRedirectStrategy().sendRedirect(request, response, targetUrl);
	}

	public void setRequestCache(RequestCache requestCache) {
		this.requestCache = requestCache;
	}
	
}
