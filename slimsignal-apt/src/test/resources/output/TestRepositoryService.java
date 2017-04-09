import com.slimgears.slimsignal.core.interfaces.RepositoryService;
import com.slimgears.slimsignal.core.interfaces.entities.EntitySet;
import com.slimgears.slimsignal.core.prototype.generated.RoleEntity;
import com.slimgears.slimsignal.core.prototype.generated.UserEntity;
import javax.annotation.Generated;

@Generated(value = "TestRepository", comments = "Repository service interface generated from TestRepository")
public interface TestRepositoryService extends RepositoryService<TestRepository> {
    EntitySet<RoleEntity> roles();
    EntitySet<UserEntity> users();
    EntitySet<ExistingEntity> existingEntities();
}
