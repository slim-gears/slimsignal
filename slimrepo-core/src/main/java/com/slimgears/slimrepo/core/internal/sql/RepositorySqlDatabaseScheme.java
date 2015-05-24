package com.slimgears.slimrepo.core.internal.sql;

import com.slimgears.slimrepo.core.interfaces.entities.Entity;
import com.slimgears.slimrepo.core.interfaces.entities.EntityType;
import com.slimgears.slimrepo.core.interfaces.fields.ComparableField;
import com.slimgears.slimrepo.core.interfaces.fields.Field;
import com.slimgears.slimrepo.core.interfaces.fields.RelationalField;
import com.slimgears.slimrepo.core.internal.interfaces.RepositoryModel;
import com.slimgears.slimrepo.core.internal.sql.interfaces.SqlDatabaseScheme;
import com.slimgears.slimrepo.core.internal.sql.interfaces.SqlStatementBuilder;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Denis on 21-May-15.
 */
class RepositorySqlDatabaseScheme implements SqlDatabaseScheme {
    private final SqlStatementBuilder.SyntaxProvider syntaxProvider;
    private final RepositoryModel repositoryModel;
    private final Map<EntityType, TableScheme> tableSchemeMap = new LinkedHashMap<>();
    private final Map<String, TableScheme> nameToTableSchemeMap = new LinkedHashMap<>();

    class EntityTypeTableScheme<TKey, TEntity extends Entity<TKey>> implements TableScheme {
        private final EntityType<TKey, TEntity> entityType;
        private final Map<Field<TEntity, ?>, FieldScheme> fieldSchemeMap = new LinkedHashMap<>();
        private final Map<String, FieldScheme> nameToFieldSchemeMap = new LinkedHashMap<>();

        class EntityFieldScheme<T> implements FieldScheme {
            private final Field<TEntity, T> field;
            private final Field.MetaInfo<T> metaInfo;

            EntityFieldScheme(Field<TEntity, T> field) {
                this.field = field;
                this.metaInfo = field.metaInfo();
            }

            @Override
            public TableScheme getTable() {
                return EntityTypeTableScheme.this;
            }

            @Override
            public String getName() {
                return metaInfo.getName();
            }

            @Override
            public String getType() {
                return syntaxProvider.typeName(field);
            }

            @Override
            public boolean isNotNull() {
                return !metaInfo.isNullable();
            }

            @Override
            public boolean isPrimaryKey() {
                return entityType.getKeyField() == field;
            }

            @Override
            public boolean isAutoIncremented() {
                return isPrimaryKey() && field instanceof ComparableField;
            }

            @Override
            public boolean isForeignKey() {
                return field instanceof RelationalField;
            }

            @Override
            public FieldScheme getRelatedForeignField() {
                if (!isForeignKey()) return null;
                RelationalField relationalField = (RelationalField)field;

                EntityType<?, ?> relatedEntityType = relationalField.metaInfo().getRelatedEntityType();
                TableScheme relatedTableScheme = getTableScheme(relatedEntityType);
                return relatedTableScheme.getKeyField();
            }
        }

        EntityTypeTableScheme(EntityType<TKey, TEntity> entityType) {
            this.entityType = entityType;
            for (Field<TEntity, ?> field : entityType.getFields()) {
                FieldScheme fieldScheme = new EntityFieldScheme<>(field);
                fieldSchemeMap.put(field, fieldScheme);
                nameToFieldSchemeMap.put(fieldScheme.getName(), fieldScheme);
            }
        }

        @Override
        public String getName() {
            return entityType.getName();
        }

        @Override
        public Map<String, FieldScheme> getFields() {
            return nameToFieldSchemeMap;
        }

        @Override
        public FieldScheme getField(String name) {
            return nameToFieldSchemeMap.get(name);
        }

        @Override
        public FieldScheme getKeyField() {
            return getFieldScheme(entityType.getKeyField());
        }

        public FieldScheme getFieldScheme(Field<TEntity, ?> field) {
            return fieldSchemeMap.get(field);
        }
    }

    public RepositorySqlDatabaseScheme(SqlStatementBuilder.SyntaxProvider syntaxProvider, RepositoryModel repositoryModel) {
        this.syntaxProvider = syntaxProvider;
        this.repositoryModel = repositoryModel;
        for (EntityType<?, ?> entityType : repositoryModel.getEntityTypes()) {
            TableScheme tableScheme = new EntityTypeTableScheme<>(entityType);
            tableSchemeMap.put(entityType, tableScheme);
        }
    }

    @Override
    public String getName() {
        return repositoryModel.getName();
    }

    @Override
    public Map<String, TableScheme> getTables() {
        return nameToTableSchemeMap;
    }

    @Override
    public TableScheme getTable(String name) {
        return nameToTableSchemeMap.get(name);
    }

    private <TKey, TEntity extends Entity<TKey>> TableScheme getTableScheme(EntityType<TKey, TEntity> entityType) {
        if (tableSchemeMap.containsKey(entityType)) {
            return tableSchemeMap.get(entityType);
        }

        TableScheme tableScheme = new EntityTypeTableScheme<>(entityType);
        tableSchemeMap.put(entityType, tableScheme);
        nameToTableSchemeMap.put(tableScheme.getName(), tableScheme);

        return tableScheme;
    }
}
