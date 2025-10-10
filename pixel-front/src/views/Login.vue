<template>
  <div class="login-container">
    <n-card class="login-card" :title="$t('auth.loginToPixelStack')">
      <n-form
        ref="formRef"
        :model="formData"
        :rules="rules"
        label-placement="top"
        require-mark-placement="right-hanging"
      >
        <n-form-item :label="$t('auth.username')" path="username">
          <n-input
            v-model:value="formData.username"
            :placeholder="$t('auth.enterUsername')"
            @keyup.enter="handleLogin"
          />
        </n-form-item>

        <n-form-item :label="$t('auth.password')" path="password">
          <n-input
            v-model:value="formData.password"
            type="password"
            show-password-on="click"
            :placeholder="$t('auth.enterPassword')"
            @keyup.enter="handleLogin"
          />
        </n-form-item>

        <n-space vertical :size="16">
          <n-button
            type="primary"
            block
            :loading="loading"
            @click="handleLogin"
          >
            {{ $t('auth.login') }}
          </n-button>

          <n-button
            text
            block
            @click="goToRegister"
          >
            {{ $t('auth.noAccount') }}
          </n-button>
        </n-space>
      </n-form>
    </n-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed } from 'vue'
import { useRouter } from 'vue-router'
import { useMessage, type FormInst, type FormRules } from 'naive-ui'
import { useI18n } from 'vue-i18n'
import { useUserStore } from '@/stores/user'
import type { LoginParams } from '@/types/user'

const { t: $t } = useI18n()
const router = useRouter()
const message = useMessage()
const userStore = useUserStore()

const formRef = ref<FormInst | null>(null)
const loading = ref(false)

const formData = reactive<LoginParams>({
  username: '',
  password: ''
})

const rules = computed((): FormRules => ({
  username: [
    {
      required: true,
      message: $t('auth.usernameRequired'),
      trigger: 'blur'
    }
  ],
  password: [
    {
      required: true,
      message: $t('auth.passwordRequired'),
      trigger: 'blur'
    },
    {
      min: 6,
      message: $t('auth.passwordMinLength'),
      trigger: 'blur'
    }
  ]
}))

const handleLogin = async () => {
  try {
    await formRef.value?.validate()
    loading.value = true

    await userStore.login(formData)
    message.success($t('auth.loginSuccess'))
    router.push('/')
  } catch (error: any) {
    if (error?.message) {
      message.error(error.message)
    }
  } finally {
    loading.value = false
  }
}

const goToRegister = () => {
  router.push('/register')
}
</script>

<style scoped>
.login-container {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 20px;
}

.login-card {
  width: 100%;
  max-width: 400px;
}
</style>
