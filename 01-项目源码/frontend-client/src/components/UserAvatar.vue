<template>
  <span class="user-avatar" :class="[`size-${size}`, variant]">
    <img :src="displayUrl" alt="" class="avatar-img" />
  </span>
</template>

<script setup>
import { computed } from 'vue'
import { resolveAvatarUrl } from '../utils/avatar.js'

const props = defineProps({
  name: { type: String, default: '' },
  avatar: { type: String, default: '' },
  size: { type: String, default: 'md' },
  variant: { type: String, default: 'default' }
})

const displayUrl = computed(() => resolveAvatarUrl(props.avatar))
</script>

<style scoped>
.user-avatar {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
  overflow: hidden;
  flex-shrink: 0;
  background: #F8FAFC;
}

.size-sm { width: 28px; height: 28px; }
.size-md { width: 36px; height: 36px; }
.size-lg { width: 48px; height: 48px; }

.user-avatar.light {
  background: rgba(255, 255, 255, 0.15);
}

.avatar-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}
</style>
