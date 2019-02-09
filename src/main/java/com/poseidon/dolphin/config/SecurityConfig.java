package com.poseidon.dolphin.config;

import java.util.Map;

import javax.servlet.Filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import com.poseidon.dolphin.simulator.member.service.MemberService;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	OAuth2ClientContext oauth2ClientContext;
	
	@Autowired
	MemberService memberService;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
				.antMatchers("/", "/login", "/webjars/**", "/error**").permitAll()
				.requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
				.anyRequest().authenticated()
			.and()
				.exceptionHandling()
				.authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login"))
			.and()
				.logout()
				.logoutUrl("/signout")
				.logoutSuccessUrl("/").permitAll()
			.and()
				.csrf()
				.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
			.and()
			.addFilterBefore(ssoFilter(), BasicAuthenticationFilter.class);
	}
	
	@Bean
	public FilterRegistrationBean<OAuth2ClientContextFilter> oauth2ClientFilterRegistration(OAuth2ClientContextFilter filter) {
		FilterRegistrationBean<OAuth2ClientContextFilter> registration = new FilterRegistrationBean<OAuth2ClientContextFilter>();
		registration.setFilter(filter);
		registration.setOrder(-100);
		return registration;
	}

	private Filter ssoFilter() {
		OAuth2ClientAuthenticationProcessingFilter naverFilter = new OAuth2ClientAuthenticationProcessingFilter(
				"/login/naver");
		OAuth2RestTemplate naverTemplate = new OAuth2RestTemplate(naver(), oauth2ClientContext);
		naverFilter.setRestTemplate(naverTemplate);
		UserInfoTokenServices tokenServices = new UserInfoTokenServices(naverResource().getUserInfoUri(),
				naver().getClientId());
		tokenServices.setPrincipalExtractor(new NaverPrincipalExtractor());
		tokenServices.setRestTemplate(naverTemplate);
		naverFilter.setTokenServices(tokenServices);
		naverFilter.setAuthenticationSuccessHandler(new SocialAuthenticationSuccessHandler(memberService));
		return naverFilter;
	}

	@Bean
	@ConfigurationProperties("naver.client")
	public AuthorizationCodeResourceDetails naver() {
		return new AuthorizationCodeResourceDetails();
	}

	@Bean
	@ConfigurationProperties("naver.resource")
	public ResourceServerProperties naverResource() {
		return new ResourceServerProperties();
	}
	
	private class NaverPrincipalExtractor implements PrincipalExtractor {
		private static final String RESPONSE_KEY = "response";
		private static final String PRINCIPAL_KEY = "id";
		
		@SuppressWarnings("unchecked")
		@Override
		public Object extractPrincipal(Map<String, Object> map) {
			if(map.containsKey(RESPONSE_KEY)) {
				Map<String, Object> response = (Map<String, Object>) map.get(RESPONSE_KEY);
				if(response.containsKey(PRINCIPAL_KEY)) {
					return response.get(PRINCIPAL_KEY);
				}
			}
			return null;
		}
		
	}

}
