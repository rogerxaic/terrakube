package org.azbuilder.api.rs.checks.vcs;

import com.yahoo.elide.annotation.SecurityCheck;
import com.yahoo.elide.core.security.ChangeSpec;
import com.yahoo.elide.core.security.RequestScope;
import com.yahoo.elide.core.security.checks.OperationCheck;
import lombok.extern.slf4j.Slf4j;
import org.azbuilder.api.plugin.security.groups.GroupService;
import org.azbuilder.api.plugin.security.user.AuthenticatedUser;
import org.azbuilder.api.rs.team.Team;
import org.azbuilder.api.rs.vcs.Vcs;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

@Slf4j
@SecurityCheck(TeamViewVcs.RULE)
public class TeamViewVcs extends OperationCheck<Vcs> {
    public static final String RULE = "team view vcs";

    @Autowired
    AuthenticatedUser authenticatedUser;

    @Autowired
    GroupService groupService;

    @Override
    public boolean ok(Vcs vcs, RequestScope requestScope, Optional<ChangeSpec> optional) {
        log.info("team view vcs {}", vcs.getId());
        List<Team> teamList = vcs.getOrganization().getTeam();
        for (Team team : teamList) {
            if(groupService.isMember(authenticatedUser.getEmail(requestScope.getUser()), team.getName()))
                return true;
        }
        return false;
    }
}
