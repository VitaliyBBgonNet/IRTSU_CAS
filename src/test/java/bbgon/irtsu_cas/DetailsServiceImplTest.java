package bbgon.irtsu_cas;

import bbgon.irtsu_cas.constants.StringConstants;
import bbgon.irtsu_cas.dto.response.SuccessResponse;
import bbgon.irtsu_cas.entity.DetailsEntity;
import bbgon.irtsu_cas.entity.UsersEntity;
import bbgon.irtsu_cas.repositories.DetailsRepository;
import bbgon.irtsu_cas.repositories.UserRepository;
import bbgon.irtsu_cas.security.CustomUserDetails;
import bbgon.irtsu_cas.services.UserService;
import bbgon.irtsu_cas.services.impl.DetailsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DetailsServiceImplTest {


    @Mock
    private DetailsRepository detailsRepository;

    @InjectMocks
    private DetailsServiceImpl detailsService;

    @Mock
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private Authentication authentication;

    private DetailsEntity detailEntity;
    private UsersEntity adminUser;
    private UUID componentId;
    private UUID userId;

    @BeforeEach
    public void setUp() {
        detailEntity = new DetailsEntity();
        componentId = UUID.randomUUID();
        detailEntity.setId(componentId);
        detailEntity.setModerationStatus(StringConstants.CANCEL_MODERATION);

        adminUser = new UsersEntity();
        userId = UUID.randomUUID();
        adminUser.setId(userId);
        adminUser.setRole(StringConstants.ADMIN_ROLE);
    }

    @Test
    @DisplayName("Success reject component")
    void testRejectComponentSuccess() {
        // Arrange
        try (MockedStatic<SecurityContextHolder> mockedStatic = mockStatic(SecurityContextHolder.class)) {
            SecurityContext securityContext = mock(SecurityContext.class);
            when(securityContext.getAuthentication()).thenReturn(authentication);
            mockedStatic.when(SecurityContextHolder::getContext).thenReturn(securityContext);

            CustomUserDetails userDetails = new CustomUserDetails(userId.toString());
            when(authentication.getPrincipal()).thenReturn(userDetails);
            when(userService.thisUser()).thenReturn(adminUser);
            when(detailsRepository.findById(componentId)).thenReturn(Optional.of(detailEntity));
            when(detailsRepository.save(any(DetailsEntity.class))).thenReturn(detailEntity);

            // Act
            SuccessResponse response = detailsService.rejectComponent(componentId.toString());

            // Assert
            assertEquals("REJECTED", response.getMsg());
            assertEquals("REJECTED", detailEntity.getModerationStatus());
            verify(detailsRepository, times(1)).save(detailEntity);
        }
    }
}