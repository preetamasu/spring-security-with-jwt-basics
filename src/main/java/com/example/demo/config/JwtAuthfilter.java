    package com.example.demo.config;

    import com.example.demo.user.UserDetailsService;
    import jakarta.servlet.FilterChain;
    import jakarta.servlet.ServletException;
    import jakarta.servlet.http.HttpServletRequest;
    import jakarta.servlet.http.HttpServletResponse;
    import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
    import org.springframework.security.core.context.SecurityContextHolder;
    import org.springframework.security.core.userdetails.UserDetails;
    import org.springframework.stereotype.Component;
    import org.springframework.web.filter.OncePerRequestFilter;

    import java.io.IOException;

    @Component
    public class JwtAuthfilter extends OncePerRequestFilter {


        private final UserDetailsService userDetailsService;

        private final JwtService jwtService;

        public JwtAuthfilter(UserDetailsService userDetailsService, JwtService jwtService) {
            this.userDetailsService = userDetailsService;
            this.jwtService = jwtService;
        }

        @Override
        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
            final String authHeader = request.getHeader("Authorization");
            final String jwtToken;
            final String userEmail;


            if(authHeader==null || !authHeader.startsWith("Bearer ")){
                filterChain.doFilter(request,response);
                return;
            }


            jwtToken = authHeader.substring(7);
            userEmail = jwtService.extractUsername(jwtToken);
            if(userEmail!= null && SecurityContextHolder.getContext().getAuthentication() == null){
                UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);

                final Boolean isTokenValid;
                if(jwtService.isTokenValid(jwtToken,userDetails)){
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }

            }
            filterChain.doFilter(request,response);
        }
        @Override
        protected boolean shouldNotFilter(HttpServletRequest request) {
            return request.getServletPath().startsWith("/auth/");
        }

    }
