<template>
  <div class="categories-page">
    <n-space vertical :size="24">
      <n-space justify="space-between" align="center">
        <h1>{{ $t('categories.title') }}</h1>
        <n-button type="primary" @click="handleCreate()">
          <template #icon>
            <n-icon><AddOutline /></n-icon>
          </template>
          {{ $t('categories.createCategory') }}
        </n-button>
      </n-space>

      <n-card>
        <n-spin :show="categoryStore.loading">
          <n-tree
            v-if="treeData.length > 0"
            block-line
            :data="treeData"
            :selectable="false"
            :render-suffix="renderTreeSuffix"
            :node-props="nodeProps"
          />

          <n-empty
            v-else
            :description="$t('categories.noCategories')"
            style="margin: 60px 0"
          >
            <template #extra>
              <n-button type="primary" @click="handleCreate()">
                {{ $t('categories.createFirstCategory') }}
              </n-button>
            </template>
          </n-empty>
        </n-spin>
      </n-card>
    </n-space>

    <!-- Create/Edit Modal -->
    <n-modal
      v-model:show="showModal"
      preset="card"
      :title="modalTitle"
      style="width: 600px"
    >
      <n-form
        ref="formRef"
        :model="formData"
        :rules="rules"
        label-placement="top"
      >
        <n-form-item :label="$t('categories.categoryName')" path="categoryName">
          <n-input
            v-model:value="formData.categoryName"
            :placeholder="$t('categories.enterCategoryName')"
          />
        </n-form-item>

        <n-form-item :label="$t('categories.parentCategory')" path="parentId">
          <n-tree-select
            v-model:value="formData.parentId"
            :options="parentCategoryOptions"
            :placeholder="$t('categories.selectParentCategory')"
            clearable
            filterable
          />
        </n-form-item>
      </n-form>

      <template #footer>
        <n-space justify="end">
          <n-button @click="showModal = false">{{ $t('common.cancel') }}</n-button>
          <n-button type="primary" :loading="submitting" @click="handleSubmit">
            {{ editingId ? $t('common.save') : $t('common.create') }}
          </n-button>
        </n-space>
      </template>
    </n-modal>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed, h, reactive } from 'vue'
import { useMessage, useDialog, NIcon, NButton, NSpace, type FormInst, type FormRules, type TreeOption } from 'naive-ui'
import { useI18n } from 'vue-i18n'
import {
  AddOutline,
  CreateOutline,
  TrashOutline,
  FolderOutline
} from '@vicons/ionicons5'
import { useCategoryStore } from '@/stores/category'
import type { Category } from '@/types/category'

const { t: $t } = useI18n()
const message = useMessage()
const dialog = useDialog()
const categoryStore = useCategoryStore()

const showModal = ref(false)
const submitting = ref(false)
const editingId = ref<number | null>(null)
const formRef = ref<FormInst | null>(null)

const formData = reactive({
  categoryName: '',
  parentId: undefined as number | undefined
})

const rules = computed((): FormRules => ({
  categoryName: [
    {
      required: true,
      message: $t('categories.categoryNameRequired'),
      trigger: 'blur'
    }
  ]
}))

const modalTitle = computed(() => {
  return editingId.value
    ? $t('categories.editCategory')
    : $t('categories.createCategory')
})

// Convert categories to tree data
const treeData = computed(() => {
  return convertToTreeData(categoryStore.categories)
})

// Get parent category options (exclude editing category and its children)
const parentCategoryOptions = computed(() => {
  const options = convertToTreeSelectOptions(categoryStore.categories)

  // If editing, filter out the editing category and its children
  if (editingId.value) {
    return filterTreeOptions(options, editingId.value)
  }

  return options
})

const convertToTreeData = (categories: Category[]): TreeOption[] => {
  return categories.map(cat => ({
    key: cat.id,
    label: cat.categoryName,
    children: cat.children ? convertToTreeData(cat.children) : undefined
  }))
}

const convertToTreeSelectOptions = (categories: Category[]): any[] => {
  return categories.map(cat => ({
    key: cat.id,
    label: cat.categoryName,
    value: cat.id,
    children: cat.children ? convertToTreeSelectOptions(cat.children) : undefined
  }))
}

const filterTreeOptions = (options: any[], excludeId: number): any[] => {
  return options
    .filter(opt => opt.value !== excludeId)
    .map(opt => ({
      ...opt,
      children: opt.children ? filterTreeOptions(opt.children, excludeId) : undefined
    }))
}

// Find category by id in tree
const findCategoryById = (categories: Category[], id: number): Category | null => {
  for (const cat of categories) {
    if (cat.id === id) return cat
    if (cat.children) {
      const found = findCategoryById(cat.children, id)
      if (found) return found
    }
  }
  return null
}

const nodeProps = ({ option }: { option: TreeOption }) => {
  return {
    onClick() {
      // Optional: handle node click
    }
  }
}

const renderTreeSuffix = ({ option }: { option: TreeOption }) => {
  return h(
    NSpace,
    {
      onClick: (e: Event) => {
        e.stopPropagation()
      }
    },
    {
      default: () => [
        h(
          NButton,
          {
            text: true,
            type: 'primary',
            size: 'small',
            onClick: (e: Event) => {
              e.stopPropagation()
              handleCreate(option.key as number)
            }
          },
          {
            default: () => $t('categories.addSubcategory'),
            icon: () => h(NIcon, null, { default: () => h(AddOutline) })
          }
        ),
        h(
          NButton,
          {
            text: true,
            type: 'primary',
            size: 'small',
            onClick: (e: Event) => {
              e.stopPropagation()
              handleEdit(option.key as number)
            }
          },
          {
            default: () => $t('common.edit'),
            icon: () => h(NIcon, null, { default: () => h(CreateOutline) })
          }
        ),
        h(
          NButton,
          {
            text: true,
            type: 'error',
            size: 'small',
            onClick: (e: Event) => {
              e.stopPropagation()
              handleDelete(option.key as number)
            }
          },
          {
            default: () => $t('common.delete'),
            icon: () => h(NIcon, null, { default: () => h(TrashOutline) })
          }
        )
      ]
    }
  )
}

onMounted(() => {
  categoryStore.fetchCategoryTree()
})

const handleCreate = (parentId?: number) => {
  editingId.value = null
  formData.categoryName = ''
  formData.parentId = parentId
  showModal.value = true
}

const handleEdit = (categoryId: number) => {
  const category = findCategoryById(categoryStore.categories, categoryId)
  if (!category) return

  editingId.value = categoryId
  formData.categoryName = category.categoryName
  formData.parentId = category.parentId
  showModal.value = true
}

const handleDelete = async (categoryId: number) => {
  const category = findCategoryById(categoryStore.categories, categoryId)
  if (!category) return

  dialog.warning({
    title: $t('categories.deleteConfirm'),
    content: $t('categories.deleteConfirmContent', { name: category.categoryName }),
    positiveText: $t('common.confirm'),
    negativeText: $t('common.cancel'),
    onPositiveClick: async () => {
      try {
        await categoryStore.deleteCategory(categoryId)
        message.success($t('categories.deleteSuccess'))
      } catch (error: any) {
        message.error(error.message || $t('categories.deleteFailed'))
      }
    }
  })
}

const handleSubmit = async () => {
  try {
    await formRef.value?.validate()
    submitting.value = true

    if (editingId.value) {
      await categoryStore.updateCategory(editingId.value, {
        categoryName: formData.categoryName,
        parentId: formData.parentId
      })
      message.success($t('categories.updateSuccess'))
    } else {
      await categoryStore.createCategory({
        categoryName: formData.categoryName,
        parentId: formData.parentId
      })
      message.success($t('categories.createSuccess'))
    }

    showModal.value = false
    formData.categoryName = ''
    formData.parentId = undefined
  } catch (error: any) {
    if (error?.message) {
      message.error(error.message)
    }
  } finally {
    submitting.value = false
  }
}
</script>

<style scoped>
.categories-page {
  max-width: 1200px;
  margin: 0 auto;
}
</style>
