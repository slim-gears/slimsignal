import com.slimgears.slimsignal.core.interfaces.RepositoryService;
import com.slimgears.slimsignal.core.interfaces.entities.EntitySet;
import com.slimgears.slimsignal.core.prototype.generated.RoleEntity;
import com.slimgears.slimsignal.core.prototype.generated.UserEntity;
import javax.annotation.Generated;

@Generated(value = "CustomOrmRepository", comments = "Repository service interface generated from CustomOrmRepository")
public interface CustomOrmRepositoryService extends RepositoryService<CustomOrmRepository> {
    EntitySet<RoleEntity> roles();
    EntitySet<UserEntity> users();
}
