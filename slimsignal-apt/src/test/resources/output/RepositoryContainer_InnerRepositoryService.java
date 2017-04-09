import com.slimgears.slimsignal.core.interfaces.RepositoryService;
import com.slimgears.slimsignal.core.interfaces.entities.EntitySet;
import com.slimgears.slimsignal.core.prototype.generated.RoleEntity;
import com.slimgears.slimsignal.core.prototype.generated.UserEntity;
import javax.annotation.Generated;

@Generated(value = "RepositoryContainer.InnerRepository", comments = "Repository service interface generated from RepositoryContainer.InnerRepository")
public interface RepositoryContainer_InnerRepositoryService extends RepositoryService<RepositoryContainer.InnerRepository> {
    EntitySet<RoleEntity> roles();
    EntitySet<UserEntity> users();
}
