<template>
  <div class="about-container">
    <n-space vertical :size="24">
      <n-page-header @back="handleBack">
        <template #title>About Page</template>
        <template #subtitle>Learn more about this project</template>
      </n-page-header>

      <n-card title="Project Information">
        <n-descriptions label-placement="left" :column="1" bordered>
          <n-descriptions-item label="Project Name">Pixel Front</n-descriptions-item>
          <n-descriptions-item label="Version">1.0.0</n-descriptions-item>
          <n-descriptions-item label="Framework">Vue 3 + TypeScript</n-descriptions-item>
          <n-descriptions-item label="Build Tool">Vite</n-descriptions-item>
          <n-descriptions-item label="UI Library">Naive UI</n-descriptions-item>
        </n-descriptions>
      </n-card>

      <n-card title="Features">
        <n-space vertical>
          <n-tag v-for="feature in features" :key="feature" type="success" :bordered="false">
            {{ feature }}
          </n-tag>
        </n-space>
      </n-card>

      <n-card title="Form Example">
        <n-form ref="formRef" :model="formValue" :rules="rules">
          <n-form-item label="Name" path="name">
            <n-input v-model:value="formValue.name" placeholder="Enter your name" />
          </n-form-item>
          <n-form-item label="Email" path="email">
            <n-input v-model:value="formValue.email" placeholder="Enter your email" />
          </n-form-item>
          <n-form-item label="Message" path="message">
            <n-input
              v-model:value="formValue.message"
              type="textarea"
              placeholder="Enter your message"
              :rows="4"
            />
          </n-form-item>
          <n-space>
            <n-button type="primary" @click="handleSubmit">Submit</n-button>
            <n-button @click="handleReset">Reset</n-button>
          </n-space>
        </n-form>
      </n-card>
    </n-space>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useMessage } from 'naive-ui'
import type { FormInst, FormRules } from 'naive-ui'

const router = useRouter()
const message = useMessage()
const formRef = ref<FormInst | null>(null)

const features = [
  'Vue 3 Composition API',
  'TypeScript Support',
  'Hot Module Replacement',
  'State Management with Pinia',
  'Vue Router Integration',
  'Axios HTTP Client',
  'ESLint & Prettier',
  'Naive UI Components'
]

const formValue = ref({
  name: '',
  email: '',
  message: ''
})

const rules: FormRules = {
  name: [{ required: true, message: 'Name is required', trigger: 'blur' }],
  email: [
    { required: true, message: 'Email is required', trigger: 'blur' },
    { type: 'email', message: 'Invalid email format', trigger: 'blur' }
  ],
  message: [{ required: true, message: 'Message is required', trigger: 'blur' }]
}

const handleBack = () => {
  router.back()
}

const handleSubmit = () => {
  formRef.value?.validate(errors => {
    if (!errors) {
      message.success('Form submitted successfully!')
      console.log('Form data:', formValue.value)
    } else {
      message.error('Please fix the form errors')
    }
  })
}

const handleReset = () => {
  formRef.value?.restoreValidation()
  formValue.value = {
    name: '',
    email: '',
    message: ''
  }
}
</script>

<style scoped>
.about-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 24px;
}
</style>
