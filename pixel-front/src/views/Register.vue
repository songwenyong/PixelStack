<template>
  <div class="register-container">
    <n-card class="register-card" title="Register for PixelStack">
      <n-form
        ref="formRef"
        :model="formData"
        :rules="rules"
        label-placement="top"
        require-mark-placement="right-hanging"
      >
        <n-form-item label="Username" path="username">
          <n-input
            v-model:value="formData.username"
            placeholder="Choose a username"
          />
        </n-form-item>

        <n-form-item label="Email" path="email">
          <n-input
            v-model:value="formData.email"
            placeholder="Enter your email"
          />
        </n-form-item>

        <n-form-item label="Nickname (Optional)" path="nickname">
          <n-input
            v-model:value="formData.nickname"
            placeholder="Enter your display name"
          />
        </n-form-item>

        <n-form-item label="Password" path="password">
          <n-input
            v-model:value="formData.password"
            type="password"
            show-password-on="click"
            placeholder="Choose a password"
          />
        </n-form-item>

        <n-form-item label="Confirm Password" path="confirmPassword">
          <n-input
            v-model:value="formData.confirmPassword"
            type="password"
            show-password-on="click"
            placeholder="Confirm your password"
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
            Register
          </n-button>

          <n-button
            text
            block
            @click="goToLogin"
          >
            Already have an account? Login here
          </n-button>
        </n-space>
      </n-form>
    </n-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { useMessage, type FormInst, type FormRules } from 'naive-ui'
import { useUserStore } from '@/stores/user'
import type { RegisterParams } from '@/types/user'

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
    return new Error('Passwords do not match')
  }
  return true
}

const rules: FormRules = {
  username: [
    {
      required: true,
      message: 'Please enter a username',
      trigger: 'blur'
    },
    {
      min: 3,
      message: 'Username must be at least 3 characters',
      trigger: 'blur'
    }
  ],
  email: [
    {
      required: true,
      message: 'Please enter your email',
      trigger: 'blur'
    },
    {
      type: 'email',
      message: 'Please enter a valid email',
      trigger: 'blur'
    }
  ],
  password: [
    {
      required: true,
      message: 'Please enter a password',
      trigger: 'blur'
    },
    {
      min: 6,
      message: 'Password must be at least 6 characters',
      trigger: 'blur'
    }
  ],
  confirmPassword: [
    {
      required: true,
      message: 'Please confirm your password',
      trigger: 'blur'
    },
    {
      validator: validatePasswordMatch,
      message: 'Passwords do not match',
      trigger: ['blur', 'input']
    }
  ]
}

const handleRegister = async () => {
  try {
    await formRef.value?.validate()
    loading.value = true

    const { confirmPassword, ...registerData } = formData
    await userStore.register(registerData)

    message.success('Registration successful! Please login.')
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
