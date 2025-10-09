<template>
  <div class="login-container">
    <n-card class="login-card" title="Login to PixelStack">
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
            placeholder="Enter your username"
            @keyup.enter="handleLogin"
          />
        </n-form-item>

        <n-form-item label="Password" path="password">
          <n-input
            v-model:value="formData.password"
            type="password"
            show-password-on="click"
            placeholder="Enter your password"
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
            Login
          </n-button>

          <n-button
            text
            block
            @click="goToRegister"
          >
            Don't have an account? Register here
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
import type { LoginParams } from '@/types/user'

const router = useRouter()
const message = useMessage()
const userStore = useUserStore()

const formRef = ref<FormInst | null>(null)
const loading = ref(false)

const formData = reactive<LoginParams>({
  username: '',
  password: ''
})

const rules: FormRules = {
  username: [
    {
      required: true,
      message: 'Please enter your username',
      trigger: 'blur'
    }
  ],
  password: [
    {
      required: true,
      message: 'Please enter your password',
      trigger: 'blur'
    },
    {
      min: 6,
      message: 'Password must be at least 6 characters',
      trigger: 'blur'
    }
  ]
}

const handleLogin = async () => {
  try {
    await formRef.value?.validate()
    loading.value = true

    await userStore.login(formData)
    message.success('Login successful!')
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
