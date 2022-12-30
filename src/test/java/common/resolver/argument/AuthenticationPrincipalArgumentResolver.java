package common.resolver.argument;

import me.tiary.security.web.userdetails.MemberDetails;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public final class AuthenticationPrincipalArgumentResolver implements HandlerMethodArgumentResolver {
    private final MemberDetails memberDetails;

    public AuthenticationPrincipalArgumentResolver(final MemberDetails memberDetails) {
        this.memberDetails = memberDetails;
    }

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return MemberDetails.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(final MethodParameter parameter,
                                  final ModelAndViewContainer mavContainer,
                                  final NativeWebRequest webRequest,
                                  final WebDataBinderFactory binderFactory) {
        return memberDetails;
    }
}