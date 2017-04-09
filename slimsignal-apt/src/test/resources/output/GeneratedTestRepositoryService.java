import com.slimgears.slimsignal.core.interfaces.entities.EntitySet;
import com.slimgears.slimsignal.core.internal.AbstractRepositoryService;
import com.slimgears.slimsignal.core.internal.interfaces.OrmServiceProvider;
import com.slimgears.slimsignal.core.internal.interfaces.SessionServiceProvider;
import com.slimgears.slimsignal.core.prototype.generated.RoleEntity;
import com.slimgears.slimsignal.core.prototype.generated.UserEntity;
import java.lang.Override;
import javax.annotation.Generated;

@Generated(value = "TestRepository", comments = "Repository service implementation generated from TestRepository")
public class GeneratedTestRepositoryService extends AbstractRepositoryService<TestRepository> implements TestRepositoryService {
    public GeneratedTestRepositoryService(OrmServiceProvider ormServiceProvider) {
        super(ormServiceProvider, GeneratedTestRepository.Model.Instance);
    }

    @Override
    protected TestRepository createRepository(SessionServiceProvider sessionServiceProvider) {
        return new GeneratedTestRepository(sessionServiceProvider);
    }

    @Override
    public final EntitySet<RoleEntity> roles() {
        return getEntitySet(RoleEntity.EntityMetaType);
    }

    @Override
    public final EntitySet<UserEntity> users() {
        return getEntitySet(UserEntity.EntityMetaType)
    }

    @Override
    public final EntitySet<ExistingEntity> existingEntities() {
        return getEntitySet(ExistingEntityMeta.EntityMetaType);
    }
}
