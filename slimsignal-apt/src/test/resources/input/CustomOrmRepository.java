import com.slimgears.slimsignal.core.interfaces.entities.EntitySet;
import com.slimgears.slimsignal.core.interfaces.Repository;
import com.slimgears.slimsignal.core.prototype.generated.UserEntity;
import com.slimgears.slimsignal.core.prototype.generated.RoleEntity;

public interface CustomOrmRepository extends Repository {
    EntitySet<RoleEntity> roles();
    EntitySet<UserEntity> users();
}
