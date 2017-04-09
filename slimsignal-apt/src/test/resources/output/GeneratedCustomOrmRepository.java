import com.slimgears.slimsignal.core.interfaces.entities.EntitySet;
import com.slimgears.slimsignal.core.internal.AbstractRepository;
import com.slimgears.slimsignal.core.internal.DefaultRepositoryModel;
import com.slimgears.slimsignal.core.internal.interfaces.SessionServiceProvider;
import com.slimgears.slimsignal.core.prototype.generated.RoleEntity;
import com.slimgears.slimsignal.core.prototype.generated.UserEntity;
import java.lang.Override;
import java.lang.String;
import javax.annotation.Generated;

@Generated(value = "CustomOrmRepository", comments = "Repository generated from CustomOrmRepository")
public class GeneratedCustomOrmRepository extends AbstractRepository implements CustomOrmRepository {
    private final EntitySet.Provider<RoleEntity> rolesEntitySet;

    private final EntitySet.Provider<UserEntity> usersEntitySet;

    GeneratedCustomOrmRepository(SessionServiceProvider sessionServiceProvider) {
        super(sessionServiceProvider);
        this.rolesEntitySet = sessionServiceProvider.getEntitySetProvider(RoleEntity.EntityMetaType);
        this.usersEntitySet = sessionServiceProvider.getEntitySetProvider(UserEntity.EntityMetaType);
    }

    @Override
    public final EntitySet<RoleEntity> roles() {
        return this.rolesEntitySet.get();
    }

    @Override
    public final EntitySet<UserEntity> users() {
        return this.usersEntitySet.get();
    }

    static class Model extends DefaultRepositoryModel {
        public static final Model Instance = new Model();

        private static final int Version = 10;

        private static final String Name = "TestRepository";

        private Model() {
            super(Name, Version, RoleEntity.EntityMetaType, UserEntity.EntityMetaType);
        }
    }
}
