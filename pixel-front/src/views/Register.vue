<template>
  <div class="register-container">
    <n-card class="register-card" :title="$t('auth.registerToPixelStack')">
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
          />
        </n-form-item>

        <n-form-item :label="$t('auth.email')" path="email">
          <n-input
            v-model:value="formData.email"
            :placeholder="$t('auth.enterEmail')"
          />
        </n-form-item>

        <n-form-item :label="$t('auth.nickname') + ' (' + $t('common.optional') + ')'" path="nickname">
          <n-input
            v-model:value="formData.nickname"
            :placeholder="$t('auth.enterNickname')"
          />
        </n-form-item>

        <n-form-item :label="$t('auth.password')" path="password">
          <n-input
            v-model:value="formData.password"
            type="password"
            show-password-on="click"
            :placeholder="$t('auth.enterPassword')"
          />
        </n-form-item>

        <n-form-item :label="$t('auth.confirmPassword')" path="confirmPassword">
          <n-input
            v-model:value="formData.confirmPassword"
            type="password"
            show-password-on="click"
            :placeholder="$t('auth.enterConfirmPassword')"
            @keyup.enter="handleRegister"
          />
        </n-form-item>

        <n-space vertical :size="16">
          <n-button
            type="primary"
            block
            :loading="loading"
            @click="handleRegister"
          >
            {{ $t('auth.register') }}
          </n-button>

          <n-button
            text
            block
            @click="goToLogin"
          >
            {{ $t('auth.hasAccount') }}
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
import type { RegisterParams } from '@/types/user'

const { t: $t } = useI18n()
const router = useRouter()
const message = useMessage()
const userStore = useUserStore()

const formRef = ref<FormInst | null>(null)
const loading = ref(false)

const formData = reactive<RegisterParams & { confirmPassword: string }>({
  username: '',
  email: '',
  nickname: '',
  password: '',
  confirmPassword: ''
})

const validatePasswordMatch = (_rule: any, value: string) => {
  if (value !== formData.password) {
    return new Error($t('auth.passwordNotMatch'))
  }
  return true
}

const rules = computed((): FormRules => ({
  username: [
    {
      required: true,
      message: $t('auth.usernameRequired'),
      trigger: 'blur'
    },
    {
      min: 3,
      message: $t('auth.usernameMinLength'),
      trigger: 'blur'
    }
  ],
  email: [
    {
      required: true,
      message: $t('auth.emailRequired'),
      trigger: 'blur'
    },
    {
      type: 'email',
      message: $t('auth.emailInvalid'),
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
  ],
  confirmPassword: [
    {
      required: true,
      message: $t('auth.passwordRequired'),
      trigger: 'blur'
    },
    {
      validator: validatePasswordMatch,
      message: $t('auth.passwordNotMatch'),
      trigger: ['blur', 'input']
    }
  ]
}))

const handleRegister = async () => {
  try {
    await formRef.value?.validate()
    loading.value = true

    const { confirmPassword, ...registerData } = formData
    await userStore.register(registerData)

    message.success($t('auth.registerSuccess'))
    router.push('/login')
  } catch (error: any) {
    if (error?.message) {
      message.error(error.message)
    }
  } finally {
    loading.value = false
  }
}

const goToLogin = () => {
  router.push('/login')
}
</script>

<style scoped>
.register-container {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 20px;
}

.register-card {
  width: 100%;
  max-width: 450px;
}
</style>
